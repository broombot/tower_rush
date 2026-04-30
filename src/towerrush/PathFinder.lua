-- PathFinder.lua
function findPaths(map, pathValue)
    -- Haal de dimensies op van de Java array
    local rows = #map
    local cols = #map[1]

    -- Zoek het startpunt (overal in de map, niet alleen de rand)
    local function findStart()
        for r = 1, rows do
            for c = 1, cols do
                -- We gebruiken tonumber() voor de zekerheid bij Java objecten
                if tonumber(map[r][c]) == tonumber(pathValue) then
                    return {r = r, c = c}
                end
            end
        end
        return nil
    end

    local startNode = findStart()

    -- Als er niks gevonden is, geef een lege tabel {} i.p.v. nil
    -- Dit voorkomt de RuntimeException in Map.java:74
    if not startNode then
        return {}
    end

    local currentPath = {}
    local visited = {}
    local current = startNode

    while current do
        -- Sla x en y op. Let op: Java gebruikt vaak 0-based, Lua is 1-based.
        -- We sturen hier de 1-based index terug.
        table.insert(currentPath, {x = current.c, y = current.r})
        visited[current.r .. "," .. current.c] = true

        local nextStep = nil
        local directions = {{0,1}, {0,-1}, {1,0}, {-1,0}}

        for _, dir in ipairs(directions) do
            local nr, nc = current.r + dir[1], current.c + dir[2]

            if nr >= 1 and nr <= rows and nc >= 1 and nc <= cols and not visited[nr .. "," .. nc] then
                local val = tonumber(map[nr][nc])
                -- Een pad gaat verder op zijn eigen waarde (pathValue) of een kruispunt (3)
                if val == tonumber(pathValue) or val == 3 then
                    nextStep = {r = nr, c = nc}
                    break
                end
            end
        end
        current = nextStep
    end

    return currentPath
end