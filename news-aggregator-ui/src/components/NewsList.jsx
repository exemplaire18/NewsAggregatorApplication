export default function NewsList({ articles, keyword, timeTaken, currentPage }) {
    return (
        <div className="mb-6">
            <div className="mb-4 text-gray-600">
                Showing results for: <span className="font-semibold">{keyword}</span>
                | Page: {currentPage} | Time: {timeTaken}ms
            </div>
            <div className="grid gap-4">
                {articles.map((article, index) => (
                    <div key={index} className="border p-4 rounded-lg hover:bg-gray-50">
                        <div className="font-medium text-sm text-gray-500 mb-1">
                            {article.newsWebsite}
                        </div>
                        <a
                            href={article.url}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="text-lg font-semibold text-blue-600 hover:underline"
                        >
                            {article.headline}
                        </a>
                        <p className="mt-1 text-gray-700">{article.description}</p>
                        <div className="mt-2 text-sm text-gray-500">
                            {new Date(article.publishedDate).toLocaleDateString()}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}