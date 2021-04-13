package rs.elfak.mosis.observers.myplaces.data

data class MyPlace (
     var name : String,
     var description : String
)
{
    override fun toString(): String {
        return name
    }
}