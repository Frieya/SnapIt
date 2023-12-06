package com.mobdeve.s12.kahitanonalang.snapit

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
                    "leaf1 desc"
                ))

                // Image 2
                data.add(ImageData(
                    "leaf2",
                    R.drawable.leaf2,
                    "leaf2 desc"
                ))

                // Image 3
                data.add(ImageData(
                    "leaf3",
                    R.drawable.leaf3,
                    "leaf3 desc"

                ))

                // Image 4
                data.add(ImageData(
                    "leaf4",
                    R.drawable.leaf4,
                    "leaf4 desc"
                ))
            }
            return data
        }

    }
}