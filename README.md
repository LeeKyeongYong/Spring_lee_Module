# Spring_lee_Module
스프링 basic Study 복습<br/>
1. DB락 <br/>

2. 단축주소는 테이블의 주키(숫자)를 사용기능추가<br/>
   - 301,302 사용 금지 및 타임리프 사용 하면 안됨 X<br/>
   - REST API로 구현 및 단축주소가 생성되면 제목만 수정할 수 있고, 다른 부분은 수정 X <br/>
   
3. RabbitMQ with STOMP 학습<br/>
   - HTTP<br/>
          - 통신 방식 : 1 : 1<br/>
          - 실패가능성 : 있음<br/>
          - 즉시응답가능 : 가능<br/><br/>
   - 큐<br/>
          - 통신 방식 : 1 : N<br/>
          - 실패가능성 : 없음<br/>
          - 신뢰성있는 통신<br/>
          - 즉시응답가능 : 불가능<br/><br/>
   - RabbitMQ<br/>
          - 사용방식 : 메세지 큐<br/>
          - 단톡방의 이름 : 익스체인지<br/>
          - 단톡방에 메세지 보내는 사람 : 프로듀서<br/>
          - 단톡방에 전달된 메세지를 읽는 사람 : 컨슈머<br/>
          - 실질적으로는 익스체인지에는 1개 이상의 큐가 연결되어 있고, 컨슈머는 큐를 섭스크라이브 한다.<br/>
          - 특정 익스체인지에 전달된 데이터가 해당 익스체인지에 연결된 큐들중 누구에게 데이터를 전달할지는 바인딩 룰이 결정된다.<br/>
          - 즉 특정 익스체인지에 전달된 데이터가 해당 익스체인지에 연결된 큐들에게 무조건 전달되는 것은 아니다.<br/><br/>
   - 카프카 미니 이벤트 추가<br/>
   
4. 메일리서치<br/>
          - Elasticsearch와 유사한 기능, 1천개의 결과를 5초 만에 찾을 수 있을 정도<br/>
          - 게시판에서 제목, 본문, 작성자 등을 검색할 때 유용하며 검색은 풀 스캔을 수행하기에 성능이 좋지 않지만, 고성능 검색을 가능<br/>
          - 특히 한글 텍스트의 경우 데이터베이스 풀텍스트 인덱스를 지원하지 않아 외부 도구가 필요한데, Elasticsearch보다 가벼우면서도 쉽게 사용할 수 있는 대안<br/>
          
5. fly클라우드<br/>
          - AWS,NCP 대신 사용.. 버전 업데이트용
   
7. 맹_Study<br/>
          - 스터디 그룹 좀더 효율적인 코드를 작성하기위해 스터디모임

8. 베이직 MSA<br/>
          - msa 프로젝트 하기위해서 베이직 학습

9. api 스터디용<br/>
          -  복습 차원

10. 디코서버 확인용<br/>
         - AWS 배포 실시간 확인하기위해 학습용
11. 슬랙서버 확인용<br/>
         - NCP 배포 실시간 확인하기위해 학습용

12. jooq_basic업로드<br/>
         - jooq_basic 업로드

13. 멱등성의 이해와 실전 적용: 토스페이먼츠 예제를 활용한 결제 시스템 구축<br/>
         - https://blog.naver.com/sleekydz86/223296131758
    
