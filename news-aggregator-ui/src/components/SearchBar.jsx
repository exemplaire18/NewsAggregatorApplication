export default function SearchBar({ keyword, setKeyword, onSearch }) {
    return (
        <div className="mb-6 flex gap-2">
            <input
                type="text"
                placeholder="Search for news (e.g., 'apple')"
                className="flex-1 p-2 border rounded-lg"
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && onSearch()}
            />
            <button
                onClick={onSearch}
                className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600"
            >
                Search
            </button>
        </div>
    );
}