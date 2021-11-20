package net.bplaced.abzzezz.videodroid.util.ui;

public abstract class CardPresentable {

    private String cardTitleText;
    private String poster;

    public CardPresentable(String cardTitleText, String poster) {
        this.cardTitleText = cardTitleText;
        this.poster = poster;
    }

    public CardPresentable(String cardTitleText) {
        this.cardTitleText = cardTitleText;
    }

    public CardPresentable() {
    }

    public String getCardTitleText() {
        return cardTitleText;
    }

    public void setCardTitleText(String cardTitleText) {
        this.cardTitleText = cardTitleText;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
