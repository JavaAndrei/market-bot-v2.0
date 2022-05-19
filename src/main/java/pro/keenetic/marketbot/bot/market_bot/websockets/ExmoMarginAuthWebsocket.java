package pro.keenetic.marketbot.bot.market_bot.websockets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pro.keenetic.marketbot.bot.market_bot.exceptions.WebsocketException;
import pro.keenetic.marketbot.bot.market_bot.markets.exmo.MarketSecurity;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ClientEndpoint
public class ExmoMarginAuthWebsocket {

    private MarketSecurity marketSecurity;

    private Session session = null;
    private final String serverName = "ExmoMarginAuth";

    private String endpoint;

    private List<String> messages;

    private boolean alive;

    private final Map<String, Object> walletUSDT = new HashMap<>();
    private final Map<String, Object> position = new HashMap<>();
    private final Map<String, Object> limitOrderSell = new HashMap<>();
    private final Map<String, Object> limitOrderBuy = new HashMap<>();

    public ExmoMarginAuthWebsocket(String endpoint) {
        this.endpoint = endpoint;
    }

    public void connect(MarketSecurity marketSecurity) {
        this.marketSecurity = marketSecurity;
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(endpoint));
        } catch (DeploymentException | URISyntaxException | IOException e) {
            System.out.println(String.format("Can't connect to server \"%s\". %s", serverName, e.getMessage()));
            throw new WebsocketException(String.format("Can't connect to server \"%s\". %s", serverName, e.getMessage()));
        }
    }

    public void disconnect() {
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                throw new WebsocketException(String.format("\"%s\" server's session closed with error. %s", serverName, e.getMessage()));
            }
        }
        marketSecurity = null;
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.printf("opening websocket \"%s\". %s%n", serverName, this);
        this.session = userSession;
        long nonce = System.currentTimeMillis() + 1119621336617600L;
        String sign = marketSecurity.signWS(marketSecurity.getPublicKey() + nonce); //String HMAC_SHA512 = "HmacSHA512";
        String key = marketSecurity.getPublicKey();
        messages.set(0, String.format(messages.get(0), key, sign, nonce));
        for (String message : messages)
            sendMessage(message);
        alive = true;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.printf("closing websocket \"%s\". Reason %s%n", serverName, reason.getReasonPhrase());
        this.session = null;
        alive = false;
    }

    @OnMessage
    public void onMessage(String message) {
        if (message.contains("error")) {
            System.out.printf("Error communicating with server \"%s\". %s%n", serverName, message);
        }
        parseMessage(message);
    }

    @OnError
    public void onError(Session userSession, Throwable error){
        System.out.printf("Error communicating with server \"%s\". %s%n", serverName, error.getMessage());
        alive = false;
    }

    public void parseMessage(String message) {
        //System.out.println(message);
        try {
            if (message.contains("margin/wallets")) {
                if (message.contains("snapshot") | message.contains("\"event\":\"update\"")) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(message).get("data").get("USDT");
                    walletUSDT.putAll(mapper.readValue(node.toString(), new TypeReference<Map<String,Object>>(){}));
                }
            } else if (message.contains("margin/positions")) {
                if (message.contains("snapshot")) {
                    ObjectMapper mapper = new ObjectMapper();
                    Iterator<JsonNode> node = mapper.readTree(message).get("data").elements();
                    while (node.hasNext()) {
                        position.putAll(mapper.readValue(node.toString(), new TypeReference<Map<String,Object>>(){}));
                    }
                }
                if (message.contains("\"event\":\"update\"")) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(message).get("data");
                    position.putAll(mapper.readValue(node.toString(), new TypeReference<Map<String,Object>>(){}));
                }
            }  else if (message.contains("margin/orders")) {
                if (message.contains("snapshot")) {
                    ObjectMapper mapper = new ObjectMapper();
                    Iterator<JsonNode> node = mapper.readTree(message).get("data").elements();
                    while (node.hasNext()) {
                        String nodeTmp = node.next().toString();
                        if (nodeTmp.contains("\"type\":\"limit_buy\"")) {
                            limitOrderBuy.putAll(mapper.readValue(nodeTmp, new TypeReference<Map<String,Object>>(){}));
                        }
                        if (nodeTmp.contains("\"type\":\"limit_sell\"")) {
                            limitOrderSell.putAll(mapper.readValue(nodeTmp, new TypeReference<Map<String,Object>>(){}));
                        }
                    }
                }
                if (message.contains("\"event\":\"update\"")) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(message).get("data");
                    String nodeTmp = node.toString();
                    if (nodeTmp.contains("\"type\":\"limit_buy\"")) {
                        limitOrderBuy.putAll(mapper.readValue(nodeTmp, new TypeReference<Map<String,Object>>(){}));
                    }
                    if (nodeTmp.contains("\"type\":\"limit_sell\"")) {
                        limitOrderSell.putAll(mapper.readValue(nodeTmp, new TypeReference<Map<String,Object>>(){}));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }



    public Map<String, Object> getWalletUSDT() {
        return walletUSDT;
    }

    public Map<String, Object> getPosition() {
        return position;
    }

    public Map<String, Object> getLimitOrderSell() {
        return limitOrderSell;
    }

    public Map<String, Object> getLimitOrderBuy() {
        return limitOrderBuy;
    }
}
