import { useState } from 'react';
import {
  Form,
  Link,
  redirect,
  useLoaderData,
  useSubmit,
} from 'react-router-dom';
import styled from 'styled-components';
import { AiFillHeart, AiOutlineHeart } from 'react-icons/ai';
import { IoMdListBox } from 'react-icons/io';
import { useRecoilValue } from 'recoil';
import { userInfo } from '../../recoil/userAtom';
import customAxios from '../../api/customAxios';
import WriterInfo from '../../components/WriterInfo';
import { StyledInput } from '../../components/Input';
import Button from '../../components/Button';
import CommentList from './CommentList';

export async function loader({ params }) {
  const response = await customAxios.get(`/boards/${params.id}`);
  const responseData = response.data;
  return { responseData };
}

export async function action({ request, params }) {
  const localToken = localStorage.getItem('localToken');
  const formData = await request.formData();
  const body = Object.fromEntries(formData);

  const response = await customAxios.post(
    `/boards/${params.id}/comments`,
    body,
    {
      headers: {
        Authorization: localToken,
      },
    },
  );
  console.log(response);

  return redirect('.');
}

function PostPage() {
  const { responseData } = useLoaderData();
  const { boardContent, comments } = responseData;
  const { head, body, liked } = boardContent;

  const user = useRecoilValue(userInfo);
  const [isLike, setIsLike] = useState(liked);

  const toggleLike = () => {
    setIsLike(!isLike);
  };

  const submit = useSubmit();
  const handleSubmit = event => {
    event.preventDefault();

    const { form } = event.currentTarget;
    const formData = new FormData(form);

    if (formData.get('body') === '') {
      alert('내용을 최소 1자 이상 입력해주세요');
      return;
    }

    submit(form);
    form.reset();
  };

  return (
    <PostPageLayout>
      <Category>커뮤니티</Category>
      <ContentContainer>
        <WriterInfo
          profileUrl={head.userProfile}
          nickname={head.nickname}
          createdAt={head.createdAt}
          viewCount={head.view}
          totalComment={head.totalComment}
          like={head.like}
          category={head.category}
          type="postWriter"
        />
        <PostTitle>{body.title}</PostTitle>
        <PostCotent>{body.content}</PostCotent>
      </ContentContainer>
      <LinkContainer>
        {user && (
          <PostLike onClick={toggleLike}>
            {isLike ? <FillHeartIcon /> : <OutlineHeartIcon />}
            <ListText>스크랩</ListText>
          </PostLike>
        )}
        <ListLink to="/posts">
          <ListIcon />
          <ListText>목록으로</ListText>
        </ListLink>
      </LinkContainer>
      <CommentCount>{head.totalComment}개의 댓글</CommentCount>
      {user ? (
        <CommentForm method="post">
          <StyledInput
            name="body"
            width="88%"
            placeholder="댓글을 작성해주세요"
          />
          <Button
            type="submit"
            width="10%"
            height="40px"
            text="등록"
            onClick={handleSubmit}
          />
        </CommentForm>
      ) : (
        <CommentForm>
          <StyledInput
            width="88%"
            placeholder="로그인 후 이용 가능합니다"
            disabled
          />
          <Button width="10%" height="40px" text="등록" disabled />
        </CommentForm>
      )}
      {comments.map(comment => (
        <CommentList key={comment.id} comment={comment} />
      ))}
    </PostPageLayout>
  );
}

const PostPageLayout = styled.main`
  display: flex;
  flex-direction: column;
  width: 100%;
  margin: 0 auto;
  @media screen and (max-width: 1024px) {
    width: 90%;
  }
`;

const Category = styled.h2`
  margin-top: 4rem;
  margin-bottom: 2.4rem;
  font-size: var(--font-size-xxl);
  font-weight: bold;
`;

const ContentContainer = styled.article`
  border-top: 1px solid var(--border-color);
`;

const PostTitle = styled.h3`
  margin-top: 3.2rem;
  margin-bottom: 2.4rem;
  font-size: var(--font-size-xl);
  font-weight: bold;
`;

const PostCotent = styled.p`
  min-height: 400px;
  font-size: var(--font-size-md);
  line-height: 2em;
`;

const LinkContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 2%;
  margin: 1.6rem 0;
`;

const PostLike = styled.button`
  display: flex;
  align-items: center;
  padding: 0;
  border: none;
  background: none;
  color: var(--font-color);
`;

const FillHeartIcon = styled(AiFillHeart)`
  width: 18px;
  height: 18px;
`;

const OutlineHeartIcon = styled(AiOutlineHeart)`
  width: 18px;
  height: 18px;
`;

const ListLink = styled(Link)`
  display: flex;
  align-items: center;
`;

const ListIcon = styled(IoMdListBox)`
  width: 20px;
  height: 20px;
`;

const ListText = styled.span`
  margin-left: 4px;
  font-size: var(--font-size-sm);
`;

const CommentCount = styled.span`
  font-size: var(--font-size-md);
`;

const CommentForm = styled(Form)`
  display: flex;
  justify-content: space-between;
  margin: 1.6rem 0;
`;

export default PostPage;
