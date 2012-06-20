package flexisim.agent;

import java.io.Serializable;
import flexisim.views.FlexiSimView;

/**
 *
 * @author harshit
 */
public class AgentManager implements Serializable {

    int l;

    public AgentManager() {
        this.l = -1;
    }
    private String agentname[] = new String[50];
    private String agenttype[] = new String[50];
    private String agentnode[] = new String[50];

    public String getAgentname(int i) {
        return agentname[i];
    }

    public String[] getAgentName() {
        return agentname;
    }

    public String getAgentnode(int i) {
        return agentnode[i];
    }

    public String[] getAgentNode() {
        return agentnode;
    }

    public String[] getAgenttype() {
        return agenttype;
    }

    public void addAgent(String aname, String atype, String node) {
        l += 1;
        if (l == 1) {
            FlexiSimView.newConnectionMenuItem.setEnabled(true);
        }
        agentname[l] = aname;
        agenttype[l] = atype;
        agentnode[l] = node;
    }

    public int getAgentNum() {
        return l + 1;
    }

    public int agentNameCheck(String aname) {
        for (int j = l; j >= 0; j--) {
            if (aname.equals(agentname[j])) {
                return 0;
            }
        }

        return 1;
    }

    public int agentCompatibility(String agent1, String agent2) {
        String atype1=null, atype2=null;
        for (int j = l; j >= 0; j--) {
            if (agentname[j].equals(agent1)) {
                atype1 = agenttype[j];
            }
            if (agentname[j].equals(agent2)) {
                atype2 = agenttype[j];
            }
        }
        if (atype1 == null ? atype2 == null : atype1.equals(atype2)) {
            return 0;
        }
        return 1;
    }
}
