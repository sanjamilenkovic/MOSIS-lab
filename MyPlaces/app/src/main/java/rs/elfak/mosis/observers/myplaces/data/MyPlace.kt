package rs.elfak.mosis.observers.myplaces.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
 class MyPlace (

)
{
    var name : String =""
    var description : String= ""
    var longitude : String = ""
    var latitude : String = ""
    @Exclude
    var key:String="";
    override fun toString(): String {
        return name
    }
}