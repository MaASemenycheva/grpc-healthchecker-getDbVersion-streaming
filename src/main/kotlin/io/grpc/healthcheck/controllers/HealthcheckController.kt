package controllers

import org.springframework.boot.configurationprocessor.json.JSONException
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

//http://localhost:8080/actuator/prometheus
//http://localhost:8080/actuator
//http://localhost:8080/actuator/health/{*path}
//http://localhost:8080/actuator/health
//http://localhost:8080/actuator/info


@Controller
@RestController
@RequestMapping("/api")
class CertificateController() {

    @Throws(JSONException::class)
    @GetMapping(value = ["/getVersion/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])

    fun getById(@PathVariable id: String?): ResponseEntity<Any?>? {
        val resp = JSONObject()
        resp.put("status", 0)
        resp.put("id", id)
        return ResponseEntity(resp.toString(), HttpStatus.OK)
    }

}

