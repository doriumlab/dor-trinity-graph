import org.neo4j.procedure.Description
import org.neo4j.procedure.Name
import org.neo4j.procedure.Procedure
import org.neo4j.procedure.UserFunction
import java.security.MessageDigest
import java.util.stream.Stream
import java.util.UUID

private class Sha256Hash {
    fun hash(data: String): String {
        val bytes = data.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

}

class HashData{
    class HashResult(dx: String){
        @JvmField()         // This is very important. If you do not use this, then Neo fails to start.
        var result:String = dx
    }

    @UserFunction(name="dor.uuid")
    @Description("creates an UUID (universally unique id)")
    fun uuid(): String {
        return UUID.randomUUID().toString()
    }

    @Procedure(name="dor.sha256pro")
    @Description("Convert data from string to Sha256 String")
    fun MakeHash(@Name("data")data: String): Stream<HashResult>{
        val h = Sha256Hash()
        return Stream.of(HashResult(h.hash(data)))
    }

    @UserFunction(name="dor.sha256fun")
    @Description("Convert data from string to Sha256 String in a function")
    fun OtherWay(@Name("data")data: String): String{
        // Note : Neo4j UserFunction only accepts a limited range of types.
        val h = Sha256Hash()
        return h.hash(data)
    }

}
