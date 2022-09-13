package com.akelius.service.typeconvertor

import androidx.room.TypeConverter
import com.akelius.service.model.countryimagesmodel.Stats

class StatsConverter {
       /* @TypeConverter
        fun fromString(attachmentIds: String): List<Stats> {

           // return attachmentIds.split(",")
        }
*/
        @TypeConverter
        fun fromArraList(attachmentIds: List<Stats>): String {
            var result = ""
            attachmentIds.forEachIndexed { index, element ->
                result += element
                if(index != (attachmentIds.size-1)){
                    result += ","
                }
            }
            return result
        }

}