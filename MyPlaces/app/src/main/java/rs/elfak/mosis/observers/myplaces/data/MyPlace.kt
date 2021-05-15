package rs.elfak.mosis.observers.myplaces.data

data class MyPlace (
     var name : String,
     var description : String
)
{
    lateinit var longitude : String
    lateinit var latitude : String
    var id : Int = 0
    override fun toString(): String {
        return name
    }
}