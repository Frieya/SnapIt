package com.mobdeve.s12.group.snapit

import java.util.Date

class DataHelper {
    companion object{
        fun generateDummyData(): ArrayList<ImageData>{
            val data = ArrayList<ImageData>()

            data.add(ImageData(
                "leaf1",
                R.drawable.leaf1,
                144.11F,
                121.01F,
                Date(2023, 1,1),
                null.toString()

            ))

            data.add(ImageData(
                "leaf2",
                R.drawable.leaf2,
                144.11F,
                121.01F,
                Date(2023, 1,1),
                null.toString()

            ))

            data.add(ImageData(
                "leaf3",
                R.drawable.leaf3,
                144.11F,
                121.01F,
                Date(2023, 1,1),
                null.toString()

            ))

            data.add(ImageData(
                "leaf4",
                R.drawable.leaf4,
                144.11F,
                121.01F,
                Date(2023, 1,1),
                null.toString()

            ))
            return data
        }

    }
}