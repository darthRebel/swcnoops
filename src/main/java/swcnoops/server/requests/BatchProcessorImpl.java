package swcnoops.server.requests;

import swcnoops.server.commands.Command;
import swcnoops.server.commands.CommandAction;
import swcnoops.server.commands.CommandFactory;
import swcnoops.server.ServiceFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BatchProcessorImpl implements BatchProcessor {
    final private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'+01:00'");
    final private CommandFactory commandFactory = new CommandFactory();

    public BatchProcessorImpl() {
    }

    @Override
    public Batch decode(String batchRequestJson) throws Exception {
        Batch batch = ServiceFactory.instance().getJsonParser().fromJsonString(batchRequestJson, Batch.class);
        return batch;
    }

    @Override
    public void decodeCommands(Batch batch) {
        for (Command command : batch.getCommands()) {
            String action = command.getAction();
            CommandAction commandAction = this.commandFactory.get(action);

            if (commandAction != null) {
                command.setCommandAction(commandAction);
            } else {
                // TODO - not implemented yet what do we do for now
            }
        }
    }

    @Override
    public BatchResponse executeCommands(Batch batch) throws Exception {
        List<ResponseData> responseDatums = new ArrayList<>(batch.getCommands().size());

        for (Command command : batch.getCommands()) {
            CommandAction commandAction = command.getCommandAction();

            if (commandAction == null) {
                System.err.println(command.getAction() + " is not supported");
            }

            CommandResult commandResult = commandAction.execute(command.getArgs(), command.getTime());
            command.setResponse(commandResult);
            ResponseData responseData = commandAction.createResponse(command, commandResult);
            responseDatums.add(responseData);
        }

        BatchResponse batchResponse = new BatchResponse(responseDatums);
        batchResponse.setProtocolVersion(ServiceFactory.instance().getConfig().PROTOCOL_VERSION);
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        batchResponse.setServerTimestamp(zonedDateTime.toEpochSecond());
        batchResponse.setServerTime(dateTimeFormatter.format(zonedDateTime));
        return batchResponse;
    }

    @Override
    public String processBatchPostBody(String batchJson) throws Exception {
        Batch batch = this.decode(batchJson);
        this.decodeCommands(batch);
        BatchResponse batchResponse = this.executeCommands(batch);
        String json = ServiceFactory.instance().getJsonParser().toJson(batchResponse);
        return json;
    }

    public static Map<String, List<String>> decodeParameters(String queryString) {
        Map<String, List<String>> parms = new HashMap<String, List<String>>();
        if (queryString != null) {
            StringTokenizer st = new StringTokenizer(queryString, "&");
            while (st.hasMoreTokens()) {
                String e = st.nextToken();
                int sep = e.indexOf('=');
                String propertyName = sep >= 0 ? decodePercent(e.substring(0, sep)).trim() : decodePercent(e).trim();
                if (!parms.containsKey(propertyName)) {
                    parms.put(propertyName, new ArrayList<String>());
                }
                String propertyValue = sep >= 0 ? decodePercent(e.substring(sep + 1)) : null;
                if (propertyValue != null) {
                    parms.get(propertyName).add(propertyValue);
                }
            }
        }
        return parms;
    }

    public static String decodePercent(String str) {
        String decoded = null;
        try {
            decoded = URLDecoder.decode(str, "UTF8");
        } catch (UnsupportedEncodingException ignored) {
            throw new RuntimeException(ignored);
        }
        return decoded;
    }
}
