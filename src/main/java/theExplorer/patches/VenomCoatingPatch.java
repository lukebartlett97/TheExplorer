package theExplorer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import theExplorer.powers.VenomCoatingPower;

@SpirePatch(clz = AbstractMonster.class, method = "damage", paramtypez = {DamageInfo.class})
public class VenomCoatingPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"p", "damageAmount"})
    public static void Insert(AbstractMonster self, DamageInfo info, AbstractPower p, @ByRef int[] damageAmount) {
        if (p instanceof VenomCoatingPower) {
            damageAmount[0] = ((VenomCoatingPower) p).onAttackToPoison(info, damageAmount[0], self);
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher("com.megacrit.cardcrawl.powers.AbstractPower", "onAttack");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
