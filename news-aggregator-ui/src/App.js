import { useState } from 'react';
import axios from 'axios';
import SearchBar from './components/SearchBar';
import NewsList from './components/NewsList';
import Pagination from './components/Pagination';

function App() {
  const [keyword, setKeyword] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [articles, setArticles] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [timeTaken, setTimeTaken] = useState(0);

  const searchNews = async (page = 1) => {
    const startTime = Date.now();
    try {
      const response = await axios.get('http://localhost:8080/api/news', {
        params: { keyword, page, size: 10 },
      });
      setArticles(response.data.articles);
      setTotalPages(response.data.totalPages);
      setCurrentPage(page);
      setTimeTaken(Date.now() - startTime);
    } catch (error) {
      console.error('Error fetching news:', error);
    }
  };

  return (
      <div className="container mx-auto p-4">
        <h1 className="text-3xl font-bold mb-6 text-center">News Aggregator</h1>
        <SearchBar
            keyword={keyword}
            setKeyword={setKeyword}
            onSearch={() => searchNews(1)}
        />
        <NewsList
            articles={articles}
            keyword={keyword}
            timeTaken={timeTaken}
            currentPage={currentPage}
        />
        <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            onPageChange={searchNews}
        />
      </div>
  );
}

export default App;