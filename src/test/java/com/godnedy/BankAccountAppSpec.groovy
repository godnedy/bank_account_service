package com.godnedy

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class BankAccountAppSpec extends Specification {

    def "contextLoads" () {
        def a=0
        when:
            def b=0

        then:
            a==b
    }
}