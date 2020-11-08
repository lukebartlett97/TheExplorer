package theExplorer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import theExplorer.actions.AlertPowerAction;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class ApplyPowerCardPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"powerToApply", "target"})
    public static void Insert(ApplyPowerAction self, AbstractPower powerToApply, AbstractCreature target) {
        AbstractDungeon.actionManager.addToBottom(new AlertPowerAction(powerToApply, target));
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCreature.class.getName(), "hasPower");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
