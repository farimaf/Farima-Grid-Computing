package biz;

import javax.persistence.*;

/**
 * Created by Farima on 7/16/2016.
 */
@Entity
@Table(name="workers")
public class Worker {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="w_id")
    private int id;

    @Column(name="w_ip")
    private String ip;


    @Column(name="w_port")
    private int port;

    public Worker(){}
    public Worker(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public int getId() {
        return id;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Worker)obj).getIp().equals(this.getIp()) && ((Worker)obj).getPort()==this.getPort();
    }
}
