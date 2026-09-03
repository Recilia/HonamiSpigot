

package rein.honami.spigot.async.thread;

public class CombatThread extends AsyncPacketThread {
    public CombatThread(String s) {
        super(s);
    }

    @Override
    public void run() {
        while (this.packets.size() > 0) {
            this.packets.poll().run();
        }
    }
} 
