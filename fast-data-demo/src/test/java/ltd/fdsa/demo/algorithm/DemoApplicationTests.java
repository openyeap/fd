package ltd.fdsa.demo.algorithm;

import com.googlecode.javaewah.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.function.Consumer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public  void  bitmap()
	{
		EWAHCompressedBitmap ewahBitmap1 = EWAHCompressedBitmap.bitmapOf(0, 2, 55, 64, 1 << 30);
		EWAHCompressedBitmap ewahBitmap2 = EWAHCompressedBitmap.bitmapOf(1, 3, 64,1 << 30);
		//bitmap 1: {0,2,55,64,1073741824}
		System.out.println("bitmap 1: " + ewahBitmap1);
		//bitmap 2: {1,3,64,1073741824}
		System.out.println("bitmap 2: " + ewahBitmap2);

		//是否包含value=64，返回为true
		System.out.println(ewahBitmap1.get(64));

		//获取value的个数，个数为5
		System.out.println(ewahBitmap1.cardinality());

		//遍历所有value
		ewahBitmap1.forEach(integer -> {
			System.out.println(integer);
		});


		//进行位或运算
		EWAHCompressedBitmap orbitmap = ewahBitmap1.or(ewahBitmap2);
		//返回bitmap 1 OR bitmap 2: {0,1,2,3,55,64,1073741824}
		System.out.println("bitmap 1 OR bitmap 2: " + orbitmap);
		//memory usage: 40 bytes
		System.out.println("memory usage: " + orbitmap.sizeInBytes() + " bytes");

		//进行位与运算
		EWAHCompressedBitmap andbitmap = ewahBitmap1.and(ewahBitmap2);
		//返回bitmap 1 AND bitmap 2: {64,1073741824}
		System.out.println("bitmap 1 AND bitmap 2: " + andbitmap);
		//memory usage: 32 bytes
		System.out.println("memory usage: " + andbitmap.sizeInBytes() + " bytes");

		//序列化与反序列化
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ewahBitmap1.serialize(new DataOutputStream(bos));
			EWAHCompressedBitmap ewahBitmap1new = new EWAHCompressedBitmap();
			byte[] bout = bos.toByteArray();
			ewahBitmap1new.deserialize(new DataInputStream(new ByteArrayInputStream(bout)));
			System.out.println("bitmap 1 (recovered) : " + ewahBitmap1new);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void roaringBitmap() {
		//向rr中添加1、2、3、1000四个数字
		RoaringBitmap rr = RoaringBitmap.bitmapOf(1,2,3,1000);
		//创建RoaringBitmap rr2
		RoaringBitmap rr2 = new RoaringBitmap();
		//向rr2中添加100-200共100个数字
		rr2.add(100L,200L);
		//返回第3个数字是1000，第0个数字是1，第1个数字是2，则第3个数字是1000
		System.out.println(	rr.select(3));

		//返回value = 2 时的索引为 1。value = 1 时，索引是 0 ，value=3的索引为2
		System.out.println(	rr.rank(2));
		//判断是否包含1000
		System.out.println(	rr.contains(1000)); // will return true
		//判断是否包含7
		System.out.println(	rr.contains(7)); // will return false

		//两个RoaringBitmap进行or操作，数值进行合并，合并后产生新的RoaringBitmap叫rror
		RoaringBitmap rror = RoaringBitmap.or(rr, rr2);

		//rr与rr2进行位运算，并将值赋值给rr
		rr.or(rr2);

		// 查看rr中存储了多少个值，1,2,3,1000和10000-12000，共2004个数字
		long cardinality = rr.getLongCardinality();
		System.out.println(cardinality);
		  cardinality = rror.getLongCardinality();
		System.out.println(cardinality);


		//遍历rr中的value
		for(int i : rr) {
			System.out.println(i);
		}
		//这种方式的遍历比上面的方式更快
		rr.forEach((Consumer<? super Integer>) i -> {
			System.out.println(i.intValue());
		});

	}
}

