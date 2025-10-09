package cz.cardgames.fortressduel.adapters.ws;


import jakarta.websocket.DeploymentException;
import org.glassfish.tyrus.server.Server;

import java.util.concurrent.CountDownLatch;

public class WsServerApp {

    public static void main (String[] args) {
        Server server = new Server("0.0.0.0", 8081, "/", null,
                PingEndpoint.class,
                GameEndpoint.class);
        CountDownLatch stop = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown requested, stopping WS server...");
            try { server.stop(); } catch (Exception ignored) {}
            stop.countDown();
        }));

        try {
            server.start();
            System.out.println("WS started: ws://0.0.0.0:8081/ws/ping");
            stop.await(); // keep running until shutdown
        } catch (DeploymentException e) {
            System.err.println("Failed to deploy WebSocket endpoints: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        } catch (InterruptedException e) {
            // Preserve interrupt status so higher-level handlers (or JVM) can react correctly
            Thread.currentThread().interrupt();
            System.err.println("Main thread interrupted; shutting down...");
        } finally {
            try { server.stop(); } catch (Exception ignored) {}
            System.out.println("WS stopped.");
        }
    }
}
