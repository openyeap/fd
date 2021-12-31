package ltd.fdsa.influxdb.test;

import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.influxdb.HolderPlace;
import ltd.fdsa.influxdb.model.Location;
import ltd.fdsa.influxdb.test.model.Person;
import ltd.fdsa.influxdb.test.service.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.*;
import java.util.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = HolderPlace.class)
public class InfluxDBClientTests {

    @Autowired
    PersonService service;

    String[] types = new String[]{"normal", "fast", "slow"};

    @Test
    public void TestWrite1KWPerDay() {
        var fromTime = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.ofHours(0));
        int count = 0;
        Random r = new Random();
        for (int j = 0; j < 86400; j++) {

            SortedSet<Person> list = new TreeSet<Person>(new Comparator<Person>() {

                @Override
                public int compare(Person o1, Person o2) {

                    return o1.getTime().compareTo(o2.getTime());
                }
            });
            for (int i = 0; i < 120; i++) {
                var entity = Person.builder()
                        .name("name" + count++)
                        .type(types[r.nextInt(types.length)])
                        .build();
                entity.setTime(fromTime.plusMillis(r.nextInt(1000)));
                var loc = new Location();
                loc.setLat(r.nextInt(1000000) / 10000);
                loc.setLon(r.nextInt(1000000) / 10000);
                entity.setPoint(loc);
                list.add(entity);
            }

            this.service.writeEntities(list.toArray(new Person[0]));
            fromTime = fromTime.plusSeconds(1);
            log.info("    datetime:   {}", LocalDateTime.ofInstant(fromTime, ZoneId.systemDefault()));

        }
    }

    @Test
    public void TestWriteAll() {

        var fromTime = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0).toInstant(ZoneOffset.ofHours(0));

        Random r = new Random();
        for (int j = 0; j < 10000; j++) {

            List<Person> list = new LinkedList<>();
            for (int i = 0; i < 10000; i++) {
                var entity = Person.builder().name("name" + r.nextInt()).type(types[r.nextInt(types.length)]).build();
                fromTime = fromTime.plusMillis(r.nextInt(1000));
                entity.setTime(fromTime);
                var loc = new Location();
                loc.setLat(r.nextInt(1000000) / 10000);
                loc.setLon(r.nextInt(1000000) / 10000);
                entity.setPoint(loc);
            }
            log.info("    datetime:   {}", LocalDateTime.ofInstant(fromTime, ZoneId.systemDefault()));
            this.service.writeEntities(list.toArray(new Person[0]));
        }
    }

    @Test
    public void TestS2() {
        var id = "91752A";
        var s2_cell_id = "17b4974";
        var lat = 7.96F;
        var lon = 38.86F;
        var s2CellId0 = S2CellId.fromToken(s2_cell_id);

        var s2CellId1 = S2CellId.fromLatLng(S2LatLng.fromDegrees(lat, lon)).parent(11);
        log.info("\n{}\n{}", s2CellId0, s2CellId1);
    }
}
