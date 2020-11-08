package theExplorer.cards.skills;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExplorer.ExplorerMod;
import theExplorer.actions.RandomiseCostAction;
import theExplorer.cards.AbstractDynamicCard;
import theExplorer.characters.TheExplorer;

import static theExplorer.ExplorerMod.makeCardPath;

public class StrangeMushrooms extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = ExplorerMod.makeID(StrangeMushrooms.class.getSimpleName());
    public static final String IMG = makeCardPath("StrangeMushrooms.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheExplorer.Enums.COLOR_GRAY;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 0;

    private static final int DRAW = 3;
    private static final int DRAW_UPG = 1;

    // /STAT DECLARATION/


    public StrangeMushrooms() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        baseMagicNumber = magicNumber = DRAW;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(magicNumber, new RandomiseCostAction()));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(DRAW_UPG);
            initializeDescription();
        }
    }
}
