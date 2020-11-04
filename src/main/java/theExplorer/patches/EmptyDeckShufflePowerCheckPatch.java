package theExplorer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theExplorer.util.OnShuffleListener;

@SpirePatch(clz = EmptyDeckShuffleAction.class, method = SpirePatch.CONSTRUCTOR)
public class EmptyDeckShufflePowerCheckPatch {
    @SpirePostfixPatch
    public static void notifyPowers(EmptyDeckShuffleAction self) {
        for(AbstractPower power : AbstractDungeon.player.powers) {
            if(power instanceof OnShuffleListener) {
                ((OnShuffleListener) power).onShuffle();
            }
        }
    }
}
