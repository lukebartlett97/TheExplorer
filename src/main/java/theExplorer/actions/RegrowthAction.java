package theExplorer.actions;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theExplorer.util.PlantService;

import java.util.ArrayList;
import java.util.function.Predicate;

public class RegrowthAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean upg;

    public RegrowthAction(boolean upgraded) {
        this.p = AbstractDungeon.player;
        setValues(this.p, AbstractDungeon.player, 1);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.upg = upgraded;
    }

    public void update() {
        if (upg) {
            upgradedAction();
        } else {
            normalAction();
        }
    }

    private void normalAction() {
        AbstractCard card = PlantService.getRandomPlant();
        returnCards(card);
    }

    private void upgradedAction() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp;
            tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            ArrayList<CustomCard> allPlants = PlantService.getAllPlants(false);
            for (CustomCard card : allPlants) {
                tmp.addToTop(card);
            }
            AbstractDungeon.gridSelectScreen.open(tmp, 1, "Choose", false);
            tickDuration();
            return;
        }
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                c.unhover();
                returnCards(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.p.hand.refreshHandLayout();
        }
        tickDuration();
    }

    class MatchesCard implements Predicate<AbstractCard> {
        AbstractCard matcher;

        MatchesCard(AbstractCard card) {
            matcher = card;
        }

        @Override
        public boolean test(AbstractCard v) {
            return matcher.cardID.equals(v.cardID);
        }
    }

    private void returnCards(AbstractCard c) {
        addToHand(c);
        long count = this.p.exhaustPile.group.stream().filter(new MatchesCard(c)).count();
        addToBot(new FetchAction(this.p.exhaustPile, new MatchesCard(c), (int) count));
        this.p.hand.refreshHandLayout();
        this.p.hand.applyPowers();
        this.isDone = true;
    }

    private void addToHand(AbstractCard c) {
        if (this.p.hand.size() == 10) {
            this.p.createHandIsFullDialog();
            this.p.discardPile.addToTop(c);
        } else {
            this.p.hand.addToTop(c);
        }
    }
}
