package theExplorer.cards.powers;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExplorer.ExplorerMod;
import theExplorer.characters.TheExplorer;
import theExplorer.powers.IvyTrapPower;
import theExplorer.powers.UpgradedIvyTrapPower;

import static theExplorer.ExplorerMod.makeCardPath;

public class IvyTrap extends CustomCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = ExplorerMod.makeID(IvyTrap.class.getSimpleName());
    public static final String IMG = makeCardPath("Pow_Beta.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheExplorer.Enums.COLOR_GRAY;
    private static final int COST = 1;
    private static final int POISON = 10;

    // /STAT DECLARATION/


    public IvyTrap() {

        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = POISON;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            addToBot(new ApplyPowerAction(p, p, new UpgradedIvyTrapPower(p, p, 1, magicNumber), 1));
        } else {
            addToBot(new ApplyPowerAction(p, p, new IvyTrapPower(p, p, 1, magicNumber), 1));
        }

    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
