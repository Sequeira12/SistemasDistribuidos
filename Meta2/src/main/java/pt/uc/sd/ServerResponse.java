package pt.uc.sd;

import java.util.ArrayList;

public class ServerResponse {
    private ArrayList<InterfaceDownloaders> downloaders;
    private ArrayList<IBarrelRemoteInterface> barrels;
    private String top10;

    public ServerResponse(ArrayList<InterfaceDownloaders> downloaders, ArrayList<IBarrelRemoteInterface> barrels, String top10) {
        this.downloaders = downloaders;
        this.barrels = barrels;
        this.top10 = top10;
    }

    // getters e setters

    public ArrayList<InterfaceDownloaders> getDownloaders() {
        return downloaders;
    }

    public void setDownloaders(ArrayList<InterfaceDownloaders> downloaders) {
        this.downloaders = downloaders;
    }

    public ArrayList<IBarrelRemoteInterface> getBarrels() {
        return barrels;
    }

    public void setBarrels(ArrayList<IBarrelRemoteInterface> barrels) {
        this.barrels = barrels;
    }

    public String getTop10() {
        return top10;
    }

    public void setTop10(String top10) {
        this.top10 = top10;
    }
}

