package byow.Core.Assets;

public class Hallway {

    private Room roomFrom;
    private Room roomTo;
    private World world;

    /**
     * A hallway object that represents a connection between roomFrom and roomTo.
     */
    public Hallway(World world, Room roomFrom, Room roomTo) {
        this.roomFrom = roomFrom;
        this.roomTo = roomTo;
        this.world = world;
        world.connect(roomFrom, roomTo);
    }
}
