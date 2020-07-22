//package ltd.fdsa.cloud.config;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.ObjectFactory;
//import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.ResolvableType;
//import org.springframework.core.codec.Decoder;
//import org.springframework.http.codec.multipart.MultipartHttpMessageWriter;
//
//public class DecoderConfig {
//
//   
//    @Bean
//    public Encoder  feignDecoder() {
//org.springframework.web.multipart.MultipartResolver
//
//    MultipartHttpMessageWriter    sss = new MultipartHttpMessageWriter();
//        ResolvableType type = new ResolvableType();
//    sss.write(input->{}, type,);
//       return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
//   }
//
//   
//
//    public ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
//       final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(new WxMappingJackson2HttpMessageConverter());
//       return new ObjectFactory<HttpMessageConverters>() {
//           @Override
//            public HttpMessageConverters getObject() throws BeansException {
//               return httpMessageConverters;
//           }
//       
//        };
//   }
//
//}
