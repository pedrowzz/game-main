import lombok.Getter;
import net.md_5.bungee.api.connection.PendingConnection;
import java.net.SocketAddress;

@Getter
public class Firewall {
    private static Firewall instance;
    
    public static Firewall getInstance() {
        if (instance == null) {
            instance = new Firewall();
        }
        return instance;
    }
    
    public void addFirewall(SocketAddress address) {
        // Implementação
    }
}