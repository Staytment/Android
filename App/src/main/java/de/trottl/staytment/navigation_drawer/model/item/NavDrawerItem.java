package de.trottl.staytment.navigation_drawer.model.item;

/**
 * @author lmatzen
 */
public class NavDrawerItem {
    private String title;
    private int icon;

    public NavDrawerItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }
}
