package rs.elfak.mosis.observers.myplaces.data

data class MyPlace (
     var name : String,
     var description : String
)
{
    var longitude : String = ""
    var latitude : String = ""
    var id : Int = 0
    override fun toString(): String {
        return name
    }
}