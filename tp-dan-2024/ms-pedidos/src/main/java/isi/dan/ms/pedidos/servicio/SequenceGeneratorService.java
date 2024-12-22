package isi.dan.ms.pedidos.servicio;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import isi.dan.ms.pedidos.modelo.DatabaseSequence;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SequenceGeneratorService {
    
    private final MongoTemplate mongoTemplate;

    public SequenceGeneratorService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Integer generateSequence(String seqName) {
        DatabaseSequence counter = mongoTemplate.findAndModify(
            query(where("_id").is(seqName)),
            new Update().inc("seq", 1),
            options().returnNew(true).upsert(true),
            DatabaseSequence.class
        );
        
        return counter.getSeq();
    }
}