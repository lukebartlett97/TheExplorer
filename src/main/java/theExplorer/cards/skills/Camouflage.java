package theExplorer.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import theExplorer.ExplorerMod;
import theExplorer.cards.AbstractDynamicCard;
import theExplorer.characters.TheExplorer;

import static theExplorer.ExplorerMod.makeCardPath;

public class Camouflage extends AbstractDynamicCard {



    // TEXT DECLARATION

    public static final String ID = ExplorerMod.makeID(Camouflage.class.getSimpleName());
    public static final String IMG = makeCardPath("Camouflage.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheExplorer.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int DEX_GAIN = 3;
    private static final int UPGRADE_PLUS_DEX = 2;


    // /STAT DECLARATION/


    public Camouflage() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = DEX_GAIN;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber)));
        addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DEX);
            initializeDescription();
        }
    }
}
