package rs.elfak.mosis.observers.myplaces.data

import com.google.firebase.database.*
import kotlin.reflect.typeOf

class MyPlacesData(
) {
    private var myPlaces: ArrayList<MyPlace> = ArrayList()


    private var myPlacesKeyIndexMapping: HashMap<String, Int> = HashMap<String, Int>()
    private var database: DatabaseReference =
        FirebaseDatabase.getInstance("https://my-places-f6dbe-default-rtdb.firebaseio.com/").reference
    private val FIREBASE_CHILD: String = "my-places"

    init {
        database.child(FIREBASE_CHILD).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                var myPlaceKey : String? = p0.key
                val myPlace = p0.getValue(MyPlace::class.java)
                myPlace!!.key  = myPlaceKey!!
                if(myPlacesKeyIndexMapping.containsKey(myPlaceKey))
                {
                    val index = myPlacesKeyIndexMapping[myPlaceKey]
                    myPlaces[index!!] = myPlace
                }
                else{
                    myPlaces.add(myPlace)
                    myPlacesKeyIndexMapping.put(myPlaceKey,myPlaces.size-1)
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            var myPlaceKey : String? = p0.key
                if(!myPlacesKeyIndexMapping.containsKey(myPlaceKey))
                {
                    var myPlace  = p0.getValue(MyPlace::class.java)
                    if (myPlace != null) {
                        if (myPlaceKey != null) {
                            myPlace.key = myPlaceKey
                        }
                    }
                    if (myPlace != null) {
                        myPlaces.add(myPlace)
                    }
                    if (myPlaceKey != null) {
                        myPlacesKeyIndexMapping.put(myPlaceKey,myPlaces.size-1)
                    }





                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val myPlaceKey = p0.key
                if(myPlacesKeyIndexMapping.containsKey(myPlaceKey))
                {
                    val index = myPlacesKeyIndexMapping.get(myPlaceKey)
                    if (index != null) {
                        myPlaces.removeAt(index)
                    }
                    recreateKeyIndexMapping()
                }
            }

        })
        database.child(FIREBASE_CHILD).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
            }

        })
    }


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

    public fun getMyPlaces(): ArrayList<MyPlace> {
        return myPlaces
    }

    public fun addNewPlace(newPlace: MyPlace) {
        var key: String? = database.push().key

        myPlaces.add(newPlace)
        if (key != null) {
            myPlacesKeyIndexMapping.put(key, myPlaces.size - 1)
        }
        if (key != null) {
            database.child(FIREBASE_CHILD).child(key).setValue(newPlace)
        }
        if (key != null) {
            newPlace.key = key
        }
    }

    public fun getPlace(index: Int): MyPlace {
        return myPlaces[index]
    }

    public fun deletePlace(index: Int) {
        database.child(FIREBASE_CHILD).child(myPlaces.get(index).key).removeValue()

        myPlaces.removeAt(index)
        recreateKeyIndexMapping()
    }
    public fun updatePlace(index:Int,nme:String,desc:String,lng:String, lat:String)
    {
        val myPlace : MyPlace = myPlaces.get(index)
        myPlace.name = nme
        myPlace.description = desc
        myPlace.latitude = lat
        myPlace.longitude = lng
        database.child(FIREBASE_CHILD).child(myPlace.key).setValue(myPlace)

    }
    public fun recreateKeyIndexMapping()
    {
        myPlacesKeyIndexMapping.clear()
        for((index,myPlace:MyPlace) in myPlaces.withIndex())
        {
            myPlacesKeyIndexMapping.put(myPlace.key,index)
        }
    }
}