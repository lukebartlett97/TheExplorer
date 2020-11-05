package theExplorer.cards.skills;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExplorer.ExplorerMod;
import theExplorer.cards.AbstractDynamicCard;
import theExplorer.characters.TheExplorer;
import theExplorer.powers.InsightPower;

import static theExplorer.ExplorerMod.makeCardPath;

public class Insight extends AbstractDynamicCard {



    // TEXT DECLARATION

    public static final String ID = ExplorerMod.makeID(Insight.class.getSimpleName());
    public static final String IMG = makeCardPath("Insight.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheExplorer.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int BLOCK = 3;
    private static final int UPGRADE_PLUS_BLOCK = 2;


    // /STAT DECLARATION/


    public Insight() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new InsightPower(p, p, 1)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
