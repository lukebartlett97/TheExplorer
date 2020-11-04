package theExplorer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExplorer.actions.BrewAction;

public abstract class BrewCard extends CustomCard {

    public BrewCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    public abstract void onAttackDiscarded(AbstractPlayer p, AbstractMonster m);

    public abstract void onSkillDiscarded(AbstractPlayer p, AbstractMonster m);

    public abstract void onPowerDiscarded(AbstractPlayer p, AbstractMonster m);

    public abstract void onOtherDiscarded(AbstractPlayer p, AbstractMonster m);

    public void onCardUse(AbstractPlayer p, AbstractMonster m) {
        if (p.hand.group.isEmpty()) {
            onOtherDiscarded(p, m);
            return;
        }
        addToBot(new DiscardAction(p, p, 1, false));
        addToBot(new BrewAction(p, m, this));
    }
}
