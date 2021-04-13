package rs.elfak.mosis.observers.myplaces.data

class MyPlacesData (
)
{
    private var myPlaces : ArrayList<MyPlace> = arrayListOf(
        MyPlace("Place A", "desc place A"),
        MyPlace("Place B", "desc place B"),
        MyPlace("Place C", "desc place C"),
        MyPlace("Place D", "desc place D"))

    companion object {
        @Volatile
        @JvmStatic
        private var INSTANCE: MyPlacesData? = null

        @JvmStatic
        @JvmOverloads
        fun getInstance(): MyPlacesData = INSTANCE ?: synchronized(this) {
            INSTANCE ?: MyPlacesData().also { INSTANCE = it }
        }
    }

    public fun getMyPlaces() : ArrayList<MyPlace>
    {
        return myPlaces
    }

    public fun addNewPlace( newPlace : MyPlace) {
        myPlaces.add(newPlace)
    }

    public fun getPlace(index : Int) : MyPlace{
        return myPlaces[index]
    }

    public fun deletePlace(index : Int) {
        myPlaces.removeAt(index)
    }
}