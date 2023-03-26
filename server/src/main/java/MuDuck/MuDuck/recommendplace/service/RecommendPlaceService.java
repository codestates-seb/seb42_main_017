package MuDuck.MuDuck.recommendplace.service;

import MuDuck.MuDuck.exception.BusinessLogicException;
import MuDuck.MuDuck.exception.ExceptionCode;
import MuDuck.MuDuck.member.service.MemberService;
import MuDuck.MuDuck.recommendplace.entity.RecommendPlace;
import MuDuck.MuDuck.recommendplace.repository.RecommendPlaceRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommendPlaceService {
    private final RecommendPlaceRepository recommendPlaceRepository;
    private final MemberService memberService;
    @Transactional
    public RecommendPlace postRecommendPlace(RecommendPlace place){
        // 해당 회원이 있는지 검증
        memberService.getMember(place.getMember().getMemberId());
        // 해당 아이디를 가진 작성글이 있는 지 검증
        verifiedRecommendPlace(place.getMember().getMemberId(), place.getMap().getMapId());

        return recommendPlaceRepository.save(place);
    }

    public RecommendPlace findRecommendPlaceToId(long id){

        return findVerifiedRecommendPlace(id);

    }

    public RecommendPlace findRecommendPlaceToMapIdAndMemberId(long memberId, long mapId){

        return findVerifiedRecommendPlace(memberId, mapId);
    }

    public RecommendPlace findVerifiedRecommendPlace(long id){
        Optional<RecommendPlace> optionalRecommendPlace = recommendPlaceRepository.findById(id);

        RecommendPlace recommendPlace = optionalRecommendPlace.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.RECOMMEND_PLACE_NOT_FOUND));

        return recommendPlace;
    }

    public RecommendPlace findVerifiedRecommendPlace(long memberId, long mapId){
        Optional<RecommendPlace> optionalRecommendPlace = recommendPlaceRepository.findByMemberIdAndMapId(memberId, mapId);

        RecommendPlace recommendPlace = optionalRecommendPlace.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.RECOMMEND_PLACE_NOT_FOUND));

        return recommendPlace;
    }

    private void verifiedRecommendPlace(long memberId, long mapId){
        Optional<RecommendPlace> optionalRp = recommendPlaceRepository.findByMemberIdAndMapId(
                memberId, mapId);
        if(optionalRp.isPresent()){
            throw new BusinessLogicException(ExceptionCode.RECOMMEND_PLACE_EXISTS);
        }
    }

}
