package theExplorer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theExplorer.ExplorerMod;
import theExplorer.characters.TheExplorer;
import theExplorer.relics.Whistle;
import theExplorer.util.TextureLoader;

public abstract class CompanionPower extends ExplorerPower {
    public AbstractCreature source;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("theExplorerResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theExplorerResources/images/powers/placeholder_power32.png");

    private int tempEvolve = 0;
    protected int timesActed = 0;

    public CompanionPower() {
        type = PowerType.BUFF;
        isTurnBased = false;
    }

    @Override
    public void onInitialApplication() {
        tempEvolve = 0;
        timesActed = 0;
        AbstractPlayer player = AbstractDungeon.player;
        for(AbstractPower power : player.powers) {
            if(power instanceof CompanionPower && !power.ID.equals(this.ID)) {
                addToTop(new RemoveSpecificPowerAction(player, player, power.ID));
            }
        }
        updateDescription();
    }

    protected int getEvolutionNumber() {
        return AbstractDungeon.actNum > 3 ? 3 + tempEvolve : AbstractDungeon.actNum + tempEvolve;
    }

    public void addTemporaryEvolve(int amount) {
        tempEvolve += amount;
        updateDescription();
        flash();
    }

    public int getTimesActed() {
        return timesActed;
    }

    public  void act() {
        AbstractPlayer player = AbstractDungeon.player;
        if(player.hasRelic(Whistle.ID)) {
            addToBot(new GainBlockAction(player, Whistle.BLOCK));
            player.getRelic(Whistle.ID).flash();
        }
        timesActed++;
        flash();
    }
}
