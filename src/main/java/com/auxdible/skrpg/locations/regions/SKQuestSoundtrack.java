package com.auxdible.skrpg.locations.regions;

import com.auxdible.skrpg.SKRPG;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import java.io.File;

public class SKQuestSoundtrack {
    private SKRPG skrpg;
    private Song willowOfTheWoodsSong;
    public SKQuestSoundtrack(SKRPG skrpg) {
        File willowOfTheWoodsFile = new File(skrpg.getDataFolder() + "/music/willowofthewoods.nbs");
        if (!willowOfTheWoodsFile.exists()) {
            throw new Error("Unable to find Willow Of The Woods soundtrack file!");
        }
        willowOfTheWoodsSong = NBSDecoder.parse(willowOfTheWoodsFile);
    }

    public Song getWillowOfTheWoodsSong() {
        return willowOfTheWoodsSong;
    }
}
