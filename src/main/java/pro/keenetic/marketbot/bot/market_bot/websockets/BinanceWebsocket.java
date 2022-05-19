package pro.keenetic.marketbot.bot.market_bot.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pro.keenetic.marketbot.bot.market_bot.exceptions.WebsocketException;
import pro.keenetic.marketbot.bot.market_bot.json.BinancePrice;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class BinanceWebsocket {

    private Session session = null;
    private final String serverName = "Binance";

    private String endpoint;

    private boolean alive;

    private BinancePrice binancePrice;

    public BinanceWebsocket(String endpoint, BinancePrice binancePrice) {
        this.endpoint = endpoint;
        this.binancePrice = binancePrice;
    }

    @PostConstruct
    private void init() {
        connect();
    }

    @PreDestroy
    private void destroy() {
        disconnect();
    }

    public void connect() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(endpoint)); //"wss://stream.binance.com:9443/ws/btcusdt@trade"
        } catch (DeploymentException | URISyntaxException | IOException e) {
            //log.logSEVERE(dao, login, String.format("Can't connect to server \"%s\". %s", serverName, e.getMessage()));
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
    }

    @OnOpen
    public void onOpen(Session userSession) {
        //log.logINFO(dao, login, String.format("opening websocket \"%s\"", serverName));
        System.out.printf("opening websocket \"%s\"%n", serverName);
        this.session = userSession;
        alive = true;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        //log.logINFO(dao, login, String.format("closing websocket \"%s\". Reason %s", serverName, reason.getReasonPhrase()));
        System.out.printf("closing websocket \"%s\". Reason %s%n", serverName, reason.getReasonPhrase());
        this.session = null;
        alive = false;
    }

    @OnMessage
    public void onMessage(String message) {
        //if (message.contains("error")) {
            //log.logSEVERE(String.format("Error communicating with server \"%s\". %s", serverName, message));
        //}
        parseMessage(message);
    }

    @OnError
    public void onError(Session userSession, Throwable error){
        //log.logSEVERE(dao, login, String.format("Error communicating with server \"%s\". %s", serverName, error.getMessage()));
        System.out.printf("Error communicating with server \"%s\". %s%n", serverName, error.getMessage());
        alive = false;
    }

    public void parseMessage(String message) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(message);
            binancePrice.setValue(node.get("p").asDouble());
            binancePrice.setTime(node.get("E").longValue());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public boolean isAlive() {
        return alive;
    }
}
