package com.mychore.mychore_server.exception.chore;

public class ChoreNotFoundException extends RuntimeException {

    public ChoreNotFoundException(Long choreId){
        super("요청한 id = "+ choreId + " 를 가진 집안일를 찾을 수 없습니다.");
    }


}
