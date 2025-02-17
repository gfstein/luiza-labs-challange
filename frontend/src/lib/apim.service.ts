class ApimService {

  async post(url: string, body: object) {
    return this.handleFetch(url, 'POST', body)
  }

  async put(url: string, body: object) {
    return this.handleFetch(url, 'PUT', body)
  }

  async patch(url: string, body: object) {
    return this.handleFetch(url, 'PATCH', body)
  }

  async delete(url: string) {
    return this.handleFetch(url, 'DELETE')
  }

  private handleFetch = async (url: string, method?: string, body?: object) => {
    const res = await fetch('/api/apim' + url, {
      method: method || 'GET',
      body: JSON.stringify(body)
    })

    if (!res.ok) {
      throw await res.json()
    }

    return res.json()
  }
}

export const apim = new ApimService();