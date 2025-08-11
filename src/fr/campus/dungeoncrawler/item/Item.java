package fr.campus.dungeoncrawler.item;

public abstract class Item {
    protected String name;
    private int id;
    private String type;
    private int attackPower;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Chaque sous-classe devra définir son propre toString()
    @Override
    public abstract String toString();

    public Object getId() {
    return id;
    }
}

