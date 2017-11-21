package pl.polsl.quizwyzwanie.views;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

class TypeFace {

    private static final HashMap<String, Typeface> cache = new HashMap<>();

    private TypeFace() {
    }

    public static Typeface get(Context c, String name) {
        synchronized (cache) {
            if (!cache.containsKey(name)) {
                Typeface t = Typeface.createFromAsset(
                        c.getAssets(), name);
                cache.put(name, t);
            }
            return cache.get(name);
        }
    }

}
