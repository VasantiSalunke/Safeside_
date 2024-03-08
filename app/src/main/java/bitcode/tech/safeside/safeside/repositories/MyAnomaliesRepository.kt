package bitcode.tech.safeside.safeside.repositories

import bitcode.tech.safeside.safeside.models.MyAnomaly

class MyAnomaliesRepository()  : Repository{

    suspend fun getMyAnomalies() : ArrayList<MyAnomaly> {

        val myAnomaly = ArrayList<MyAnomaly>()
        myAnomaly.add(MyAnomaly("Deep Potholes", 120,12,"12 Jan 2024"))
        myAnomaly.add(MyAnomaly("Puncture Scammers", 120,12,"12 Jan 2024"))
        myAnomaly.add(MyAnomaly("Bridge broken", 120,12,"12 Jan 2024"))
        myAnomaly.add(MyAnomaly("Street Light is not working", 120,12,"12 Jan 2024"))
        myAnomaly.add(MyAnomaly("Traffic Police", 120,12,"12 Jan 2024"))

        return myAnomaly

    }
}