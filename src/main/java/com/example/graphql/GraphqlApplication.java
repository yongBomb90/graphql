package com.example.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.graphql.model.Box;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class GraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlApplication.class, args);
    }


    /***
     * 데이터를 처리할 리파지토리
     */
    @Repository
    public class BoxRepository {
        Map<String, Box> boxMap = new HashMap<>();
        public Box getBox(String name) {
            return boxMap.getOrDefault(name, null);
        }
        public int countBox() {
            return boxMap.size();
        }
        public boolean existBox(String name) {
            return boxMap.get(name) != null;
        }
        public boolean putBox(Box box) {
            if (boxMap.get(box.getName()) != null)
                return false;

            this.boxMap.put(box.getName(), box);
            return true;
        }

        public boolean deleteBox(String name) {
            if (boxMap.get(name) == null)
                return false;

            boxMap.remove(name);
            return true;
        }
    }

    /**
     * 데이터 조회 즉, 쿼리 처리를위한 리졸버
     */
    @Component
    public class BoxGraphQLQueryResolver implements GraphQLQueryResolver {
        public int countBox() {return boxRepository.countBox();}
        public boolean existBox(String name) {return boxRepository.existBox(name);}
        public Box getBox(String name) {return boxRepository.getBox(name);}

        @Autowired
        BoxRepository boxRepository;
    }

    /**
     * 데이터 입력 및 수정 DML 처리를 위한 리졸버
     */
    @Component
    public class BoxGraphQLMutationResolver implements GraphQLMutationResolver {
        public boolean putBox(Box box) {return boxRepository.putBox(box);}
        public boolean delBox(String name) {return boxRepository.deleteBox(name);}

        @Autowired
        BoxRepository boxRepository;
    }


}
