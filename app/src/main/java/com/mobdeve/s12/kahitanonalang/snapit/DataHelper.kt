package com.mobdeve.s12.kahitanonalang.snapit

import java.util.Date

class DataHelper {
    companion object{
        fun generateDummyData(): ArrayList<ImageData>{
            val data = ArrayList<ImageData>()

            val numLoop = 1
            for (i in 1..numLoop) {

                // Image 1
                data.add(ImageData(
                    "leaf1",
                    R.drawable.leaf1,
                    144.11F,
                    121.01F,
                    Date(2023 - 1900, 1,1),
                    "tiny, tree",
                    "leaf1 desc"
                ))

                // Image 2
                data.add(ImageData(
                    "leaf2",
                    R.drawable.leaf2,
                    144.11F,
                    121.01F,
                    Date(2023, 1,1),
                    "sticks, shrub",
                    "leaf2 desc"
                ))

                // Image 3
                data.add(ImageData(
                    "leaf3",
                    R.drawable.leaf3,
                    144.11F,
                    121.01F,
                    Date(2023, 1,1),
                    "pot, green",
                    "leaf3 desc"

                ))

                // Image 4
                data.add(ImageData(
                    "leaf4",
                    R.drawable.leaf4,
                    144.11F,
                    121.01F,
                    Date(2023, 1,1),
                    "pot, droopy",
                    "leaf4 desc"
                ))
            }
            return data
        }

    }
}