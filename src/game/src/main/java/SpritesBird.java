public enum SpritesBird {

    BIRDY(new String[]{
            "****__",
            "___( O)>",
            "( <_. )",
            "*'___´"
    }),

    DEADBIRDY(new String[]{
            "****__",
            "___( X)>",
            "( <_. )",
            "*'___´"
    });

    private final String[] sprite;

    SpritesBird(String[] sprite) {
        this.sprite = sprite;
    }

    public String[] getSprite() {
        return sprite;
    }

}
