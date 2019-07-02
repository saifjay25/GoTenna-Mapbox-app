import com.mycode.myapplication.entity.PinData

class UtilTest {
    companion object{
        val pinData = PinData(1, "name",0.0,0.0)
        val pinData2 = PinData(2, "name2",1.0,1.0)
        var pinList : MutableList<PinData> = mutableListOf(pinData, pinData2)
    }
}