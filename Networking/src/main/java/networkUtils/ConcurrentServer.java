package networkUtils;

import service.ITeledonService;

import java.net.Socket;

public class ConcurrentServer extends AbstractConcurrentServer {
    private ITeledonService serverService;
    public ConcurrentServer(int port, ITeledonService serverService) {
        super(port);
        this.serverService = serverService;
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientWorker worker=new ClientWorker(this.serverService, client);
        Thread tw = new Thread(worker);
        return tw;
    }
}
